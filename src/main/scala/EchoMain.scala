
import akka.actor.{Actor, ActorSystem, Props}
import scala.io.StdIn
//import MedianFilter

case class Message(msg:String)
case object Bye

class ImageActor extends Actor {

  def receive ={
    //should handle here between seq and parallel
    //case Message(s) => println(s"Something with $s\n")
    case Bye => println("Bye!")
    case "p"=> {
      val t0 = System.nanoTime()
      val mf =  new MedianFilter()
      mf.do_all_par("test.jpg")
      val t1 = System.nanoTime()
      val fin = t1-t0
      println(s"Parallel Result time:$fin\n")

      }
    case "s" =>{
      val t0 = System.nanoTime()
      val mf = new MedianFilter()
      mf.do_all_seq("test.jpg")
      val t1 = System.nanoTime()
      val fin = t1-t0
      println(s"Sequential Result time:$fin\n")
    }
            //do parallel stuff here
    case _ => println("Unknown param received")
  }

}

object EchoMain extends App{

  val system = ActorSystem("Parallel Image Processing System")

  // create and start the actor
  val echoActor = system.actorOf(Props[ImageActor], name = "ImageActor")

  // prompt the user for input
  var input = ""
  while (input != "q") {
    print("Type name Image to process (q to quit): ")
    input = StdIn.readLine()
    echoActor ! input
    // a brief pause so the actor can print its output
    // before this loop prints again
    Thread.sleep(200)
  }

  echoActor ! Bye

  // shut down the system
  system.terminate()

}
