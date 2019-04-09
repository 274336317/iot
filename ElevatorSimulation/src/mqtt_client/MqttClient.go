package mqtt_client

import (
	"elevator"
	MQTT "github.com/eclipse/paho.mqtt.golang"
	"log"
	"time"
)

var mClient MQTT.Client = nil

const addr string = "tcp://192.168.0.109:1883"

const TOPIC string = "elevator/status"

const USERNAME string = "mqtt"

const PASSWORD string = "mqtt"

func initClient() bool {

	opts := MQTT.NewClientOptions().AddBroker(addr)
	opts.SetPassword(PASSWORD)
	opts.SetUsername(USERNAME)
	opts.SetClientID("elevator")

	mClient = MQTT.NewClient(opts)
	if token := mClient.Connect(); token.Wait() && token.Error() != nil {
		log.Fatalf("Connect To %s Failed.Error:\n", addr, token.Error())
		return false
	}
	log.Printf("Connect To %s Succ:\n", addr)
	return true
}

func sendMsgToServer() bool {
	
	text := elevator.ToJson()
	token := mClient.Publish("elevator/status", 0, false, text)
	token.Wait()
	
	log.Printf("Send Msg To Topic %s.Data:\n %s", TOPIC, text)
	
	if token.Error() != nil{
		log.Fatalf("Send Msg To Topic %s Failed.Error:\n", addr, token.Error())
		return false
	}
	
	return true

}

func StartMqttClient() {
	initClient()

	for {
		time.Sleep(5 * time.Second)
		sendMsgToServer()
		log.Println("Send Msg To MQTT Server")
	}
}
