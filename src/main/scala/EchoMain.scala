
import akka.actor.{Actor, ActorSystem, Props}
import javax.swing.{JFrame, JLabel, JPanel, ImageIcon}
import java.awt.{GridLayout, Image}
import java.awt.image.BufferedImage
import scala.io.StdIn
//import MedianFilter

case class Message(msg:String)
case object Bye

class ImageActor extends Actor {

  val mf = new MedianFilter()

  def sequential():BufferedImage ={
    val t0 = System.nanoTime()
//    val mf = new MedianFilter()
    val img = mf.do_all_seq("angular.png")
    val t1 = System.nanoTime()
    val fin = t1-t0
    println(s"Sequential Result time:$fin\n")
    return img
  }

  def parallel():BufferedImage = {

    val t0 = System.nanoTime()
    val img = mf.do_all_par("test.jpg")
    val t1 = System.nanoTime()
    val fin = t1-t0
    println(s"Parallel Result time:$fin\n")

    return img
  }

  def visualize(title:String, image:BufferedImage) ={
    val frame = new JFrame()

    frame.setTitle(title)
   // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    val panel = new JPanel()
    panel.setLayout(new GridLayout(0, image.getWidth()))

    //val list = getComponents


    panel.add(new JLabel(new ImageIcon(image)))


    frame.add(panel)
    frame.setVisible(true)
    frame.pack()

  }

  def receive ={
    //should handle here between seq and parallel
    //case Message(s) => println(s"Something with $s\n")
    case Bye => println("Bye!")
    case "p"=> {
         visualize("parallel_result", parallel())

      }
    case "s" =>{
      visualize("sequential_result", sequential())

    }
            //do parallel stuff here
    case _ => println("Unknown param received")
  }

}

object EchoMain extends App{

  val system = ActorSystem("ImageProcessingSystem")

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
