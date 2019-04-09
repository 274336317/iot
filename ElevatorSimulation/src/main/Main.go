package main

import (
//"fmt"
//	"encoding/json"
//	MQTT "github.com/eclipse/paho.mqtt.golang"
//  "os"
  "time"
//  "math/rand"
//  "math"
//    "elevator"
  "floor"
  "elevator"
  "mqtt_client"

)


func main(){
	go floor.PersonActivity()
	go elevator.StartElevatorActivity()
	go mqtt_client.StartMqttClient()
	
	for ;;{
		time.Sleep(time.Duration(3000)*time.Millisecond)
	}	
}