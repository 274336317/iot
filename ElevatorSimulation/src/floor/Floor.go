package floor

import(
	"time"
	"math/rand"
	"log"
	"sync"
)

//层高，单位是厘米
const FLOOR_HEIGHT = 300

//楼层数量
const FLOOR_NUM = 60

//乘客
type Person struct{
	Age uint8//年龄
	Sex uint8//性别
	StartFloor uint8//起始楼层
	DestFloor uint8//目的楼层
	Direction uint8//1表示需要上行，2表示需要下行
}
//等待乘坐电梯的人员:Key表示所在的楼层,Value表示等待的人员集合
var WaitingPersons map[uint8][]Person = make(map[uint8][]Person)

var mutex sync.Mutex

//是否有人想上升
func HasPersonUp(floorNum uint8)bool{
	mutex.Lock()
	defer mutex.Unlock()
	
	persons := WaitingPersons[floorNum]
	
	for _,val := range persons{
		if val.Direction == 1{
			return true
		}
	}
	
	return false
}

//是否有人想下降
func HasPersonDown(floorNum uint8)bool{
	mutex.Lock()
	defer mutex.Unlock()
	
	persons := WaitingPersons[floorNum]
	
	for _,val := range persons{
		if val.Direction == 2{
			return true
		}
	}
	
	return false
}

//获取指定楼层的上升人员
func GetUpPersons(floorNum uint8)[]Person{
	mutex.Lock()
	defer mutex.Unlock()
	
	persons := WaitingPersons[floorNum]
	
	upPersons := make([]Person,1)
	
	
	for _,val := range persons{
		if val.Direction == 1{
			_=append(upPersons,val)
		}
	
	}
	
	//删除掉上电梯的人
	for _, val := range upPersons{
		for index2,val2 := range persons{
			if(val2 == val){
				persons = append(persons[:index2],persons[index2+1:]...)
			}
		}
	}
	
	WaitingPersons[floorNum] = persons
	
	
	return upPersons
}

//获取指定楼层的下降人员
func GetDownPersons(floorNum uint8)[]Person{
	mutex.Lock()
	defer mutex.Unlock()
	
	persons := WaitingPersons[floorNum]
	
	downPersons := make([]Person,1)
	
	for _,val := range persons{
		if val.Direction == 2{
			_=append(downPersons,val)
		}
	
	}
	
	//删除掉上电梯的人
	for _, val := range downPersons{
		for index2,val2 := range persons{
			if(val2 == val){
				persons = append(persons[:index2],persons[index2+1:]...)
			}
		}
	}
	
	WaitingPersons[floorNum] = persons
	
	
	return downPersons
}

func Generate(){
	
	mutex.Lock()
	defer mutex.Unlock()
	//随机选择一个楼层
	floorNum := uint8(rand.Intn(FLOOR_NUM))
	//随机生成需要乘坐电梯上行的人
	personUpNum := uint8(rand.Intn(5))
	//随机生成需要乘坐电梯下行的人
	personDownNum := uint8(rand.Intn(5))
	
	log.Println("-----------------------------");
	log.Println("floorNum:", floorNum)
	log.Println("personUpNum:", personUpNum)
	log.Println("personDownNum:", personDownNum)
	
	log.Println("UP-----");
	
	//随机生成需要上行的人的信息
	for i:=uint8(0);i<personUpNum;i++{
		start:=floorNum
		end:=uint8(rand.Intn(59))+start
		if start+ end > 59{
			end = 59
		}
		person:=Person{
			Age:uint8(rand.Intn(60) + 5),
			Sex:uint8(rand.Intn(60)%3),
			StartFloor:uint8(start),
			DestFloor:uint8(end),
			Direction:1,
		}
		
		_=append(WaitingPersons[floorNum], person)
		log.Println("person:",i, person)
	
	}
	
	log.Println("DOWN-----");
	
	//随机生成需要下行的人的信息
	for i:=uint8(0);i<personDownNum;i++{
		if floorNum == 0{
			break
		}
		start:=floorNum
		end:=uint8(rand.Intn(59))
		if start < end{
			end = start - 1
		}
		person:=Person{
			Age:uint8(rand.Intn(60) + 5),
			Sex:uint8(rand.Intn(60)%3),
			StartFloor:uint8(start),
			DestFloor:uint8(end),
			Direction:2,
		
		}
		
		log.Println("person:",i, person)
		_=append(WaitingPersons[floorNum], person)
	
	}
}

//楼层里的人员乘坐电梯的活动
func PersonActivity(){
	for i :=0;i<60;i++{
		WaitingPersons[uint8(i)]=make([]Person,10)
	}
	for ;;{
		time.Sleep(5*time.Second)
		Generate()
	}
}