package elevator

import (
	"encoding/json"
	"floor"
	"log"
	"math/rand"
	"time"
)

//电梯的运行速度，单位是厘米
const SPEED = 100

//加速度
const Accelerated_speed = 50

//楼层高度，单位厘米
const FLOOR_HEIGHT = 300

//定义电梯对象
type Elevator struct {
	Sn           string //电梯编号
	Address      string //电梯地址
	Producer     string //生产厂商
	Producer_tel string //生产厂商电话号码
	Status       uint8  //电梯的状态：1表示正在上升、2表示正在下降、3表示停靠
	Direction    uint8  //电梯运行方向：1表示向上运行、2表示向下运行

	LeftDoor  uint8 //1表示关闭，2表示打开，3表示正在关闭，4表示正在打开
	RightDoor uint8 //1表示关闭，2表示打开，3表示正在关闭，4表示正在打开

	Persons map[uint8][]floor.Person //电梯乘员数量,Key表示目的楼层，value表示到目的楼层的乘员列表

	Temperature       uint8   //电梯内部温度
	Humidity          uint8   //湿度
	Speed             float32 //电梯的当前速度
	Accelerated_speed uint16  //电梯当前的加速度

	Smoky   bool   //是否有烟雾
	Fire    bool   //是否着火
	Water   bool   //是否有水
	Floor   uint8  //电梯当前的楼层
	Weight  uint16 //电梯当前的载重
	Shaking bool   //电梯是否在震动

	BerthFloors []uint8 //电梯需要停靠的楼层号
}

var elevator Elevator

//将电梯当前状态转换为JSON格式数据
func ToJson() string {
	data, _ := json.Marshal(elevator)
	
	return string(data)
}

//电梯上行
func Up(ele *Elevator, floor_start uint8, floor_stop uint8) {

	log.Printf("Up: From %d To %d\n", floor_start, floor_stop)

	ele.Status = 1
	ele.LeftDoor = 1
	ele.RightDoor = 1
	ele.Speed = 0
	i := 0
	//急速到100所用的时间
	t1 := SPEED * 1000 / Accelerated_speed
	//在加速阶段所行驶的路程
	d1 := Accelerated_speed * Accelerated_speed * t1 / 2
	//需要行驶的总路程
	d := int(floor_stop-floor_start) * FLOOR_HEIGHT

	//需要匀速行驶的路程
	d2 := d - d1*2

	//行驶完匀速路程花费的时间
	t2 := d2 * 1000 / SPEED

	ele.Accelerated_speed = 50

	//加速阶段
	for ; i < t1; i++ {
		ele.Speed += 50 / 1000
		time.Sleep(time.Duration(1) * time.Millisecond)
	}

	//匀速行驶阶段
	ele.Accelerated_speed = 0
	ele.Speed = 100
	time.Sleep(time.Duration(t2) * time.Millisecond)

	//减速阶段
	ele.Accelerated_speed = 50
	for ; i < t1; i++ {
		ele.Speed -= 50 / 1000
		time.Sleep(time.Duration(1) * time.Millisecond)
	}

	ele.Accelerated_speed = 0
	ele.Speed = 0

	//停靠
	Berth(ele, floor_stop)

}

//电梯下行
func Down(ele *Elevator, floor_start uint8, floor_stop uint8) {

	log.Printf("Down: From %d To %d\n", floor_start, floor_stop)

	ele.Status = 1
	ele.LeftDoor = 1
	ele.RightDoor = 1
	ele.Speed = 0
	i := 0
	//急速到100所用的时间
	t1 := SPEED * 1000 / Accelerated_speed
	//在加速阶段所行驶的路程
	d1 := Accelerated_speed * Accelerated_speed * t1 / 2
	//需要行驶的总路程
	d := int(floor_stop-floor_start) * FLOOR_HEIGHT

	//需要匀速行驶的路程
	d2 := d - d1*2

	//行驶完匀速路程花费的时间
	t2 := d2 * 1000 / SPEED

	ele.Accelerated_speed = 50

	//加速阶段
	for ; i < t1; i++ {
		ele.Speed += 50 / 1000
		time.Sleep(time.Duration(1) * time.Millisecond)
	}

	//匀速行驶阶段
	ele.Accelerated_speed = 0
	ele.Speed = 100
	time.Sleep(time.Duration(t2) * time.Millisecond)

	//减速阶段
	ele.Accelerated_speed = 50
	for ; i < t1; i++ {
		ele.Speed -= 50 / 1000
		time.Sleep(time.Duration(1) * time.Millisecond)
	}

	ele.Accelerated_speed = 0
	ele.Speed = 0

	//停靠
	Berth(ele, floor_stop)
}

//获取下一个电梯停靠的楼层
func GetNextBerthFloor(ele *Elevator, currentFloor uint8) uint8 {

	floorNum := currentFloor
	if ele.Direction == 1 {
		//上行，找出最小值
		for i := currentFloor + 1; i < 60; i++ {
			num := len(ele.Persons[i])
			if num > 0 {
				floorNum = i
			}
		}
	} else if ele.Direction == 2 {
		//下行，找出最大值
		for i := currentFloor - 1; i >= 0; i-- {
			num := len(ele.Persons[i])
			if num > 0 {
				floorNum = i
			}
		}
	}

	return floorNum
}

//电梯停靠
func Berth(ele *Elevator, floorNum uint8) {

	log.Printf("Berth At: %d\n", floorNum)

	ele.Floor = floorNum

	//到达楼层后，开门
	ele.LeftDoor = 4
	ele.RightDoor = 4
	//打开门花费2秒钟
	time.Sleep(time.Duration(2000) * time.Millisecond)

	//门处于打开状态
	ele.LeftDoor = 2
	ele.RightDoor = 2

	//检查是否有乘员在本楼层下电梯
	if _, ok := ele.Persons[floorNum]; ok {

		ele.Persons[floorNum] = make([]floor.Person, 1)
	}

	//停靠至少5秒钟
	berthTime := rand.Intn(20)

	for berthTime < 5 {
		berthTime = rand.Intn(20)
	}

	//检查电梯里面是否还有人
	if remains := len(ele.Persons); remains == 0 {

		for {
			//如果电梯里面没人，则需要检查电梯继续上升还是下降或者停止不动
			if floor.HasPersonDown(floorNum - 1) {
				ele.Direction = 2
				break
			} else if floor.HasPersonUp(floorNum - 1) {
				ele.Direction = 1
				break
			} else {
				log.Printf("No Body Take Elevator\n")
				ele.Direction = 0
				//如果没有人乘坐电梯，则停靠在当前楼层
				time.Sleep(1 * time.Second)
				continue
			}
		}
	}

	if ele.Direction == 1 {
		//上升
		//人员上电梯
		persons := floor.GetUpPersons(floorNum)
		for _, val := range persons {
			dest := val.DestFloor
			_ = append(ele.Persons[dest], val)
		}
	} else if ele.Direction == 2 {
		//下降
		//人员上电梯
		persons := floor.GetDownPersons(floorNum)
		for _, val := range persons {
			dest := val.DestFloor
			_ = append(ele.Persons[dest], val)
		}
	}

	nextFloorNum := GetNextBerthFloor(ele, floorNum)
	if ele.Direction == 1 {
		Up(ele, floorNum, nextFloorNum)
	} else if ele.Direction == 2 {
		Down(ele, floorNum, nextFloorNum)
	}
}

func StartElevatorActivity() {
	//初始停靠在一楼
	elevator = Elevator{
		Sn:           "123-456-789",
		Floor:        1,
		Status:       3,
		Address:      "爱上了飞机到了撒娇地方刘三姐",
		Producer:     "Hitachi",
		Producer_tel: "12312312312",
	}
	log.Println("Elevator Start...")
	Berth(&elevator, 1)
}
