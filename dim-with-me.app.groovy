/**
 *  DimWithMe.v02.app.groovy
 *  Dim With Me
 *
 *  Author: todd@wackford.net
 *  Date: 2013-11-12
 */
/**
 *  App Name:   Dim With Me
 *
 *  Author: 	Todd Wackford
 *				twack@wackware.net
 *  Date: 		2013-11-12
 *  Version: 	0.1
 *  			a) Created
 *
 *  Date: 		2014-10-09
 *  Version: 	0.2
 *  			a) Update with Metadata
 *  
 *  Use this program with a virtual dimmer as the master for best results.
 *
 *  This app lets the user select from a list of dimmers to act as a triggering
 *  master for other dimmers or regular switches. Regular switches come on
 *  anytime the master dimmer is on or mnaster dimmer level is set to more than 0%.
 *  of the master dimmer. They go off when the master is off.
 *
 *  
 */


// Automatically generated. Make future change here.
definition(
    name: "Dim With Me",
    namespace: "",
    author: "todd@wackford.net",
    description: "Follows the dimmer level of another dimmer",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png"
)

preferences {
	section("When this...") { //use this program with a virtual dimmer
		input "masters", "capability.switchLevel", 
			multiple: false, 
			title: "Master Dimmer Switch...", 
			required: true
	}
    
    section("And these will follow with dimming level...") {
		input "slaves", "capability.switchLevel", 
			multiple: true, 
			title: "Slave Dimmer Switch(es)...", 
			required: true
	}
    
	section("Then these on/off switches will follow with on/off...") {
		input "slaves2", "capability.switch", 
			multiple: true, 
			title: "Slave On/Off Switch(es)...", 
			required: false
	}
}

def installed()
{
	subscribe(masters, "switch.on", switchOnHandler)
    subscribe(masters, "switch.off", switchOffHandler)
    subscribe(masters, "switch.setLevel", switchSetLevelHandler)
    subscribe(masters, "switch", switchSetLevelHandler)
}

def updated()
{
	unsubscribe()
	subscribe(masters, "switch.on", switchOnHandler)
    subscribe(masters, "switch.off", switchOffHandler)
    subscribe(masters, "switch.setLevel", switchSetLevelHandler)
    log.info "subscribed to all of switches events"
}

def switchSetLevelHandler(evt)
{	
    def level = evt.value.toFloat()
    level = level.toInteger()
    log.info "switchSetLevelHandler Event: ${level}"
    slaves?.setLevel(level)
}

def switchOffHandler(evt) {
	log.info "switchoffHandler Event: ${evt.value}"
    slaves?.off()
    slaves2?.off()
}

def switchOnHandler(evt) {
	log.info "switchOnHandler Event: ${evt.value}"
    def dimmerValue = masters.latestValue("level") //can be turned on by setting the level
    slaves?.on()
    slaves2?.on()
}
