
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

  def sequential(filename:String):BufferedImage ={
    val t0 = System.nanoTime()
    //    val mf = new MedianFilter()
    val img = mf.do_all_seq(filename)
    val t1 = System.nanoTime()
    val fin = t1-t0
    println(s"Sequential Result time:$fin\n")
    return img
  }

  def parallel(filename:String):BufferedImage = {

    val t0 = System.nanoTime()
    val img = mf.do_all_par(filename)
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



    panel.add(new JLabel(new ImageIcon(image)))


    frame.add(panel)
    frame.setVisible(true)
    frame.pack()

  }

  def receive ={
    //should handle here between seq and parallel
    case s:String => visualize("sequential_result", sequential(s))
    case Bye => println("Bye!")
//    case "p"=> {
//      visualize("parallel_result", parallel())
//
//    }
//    case "s" =>{
//      visualize("sequential_result", sequential())
//
//    }
    //do parallel stuff here
    case _ => println("Unknown param received")
  }

}


class ImageActor2 extends Actor {

  val mf = new MedianFilter()

  def sequential(filename:String):BufferedImage ={
    val t0 = System.nanoTime()
    //    val mf = new MedianFilter()
    val img = mf.do_all_seq(filename)
    val t1 = System.nanoTime()
    val fin = (t1-t0) * 1.0 / 1E9
    println(s"Sequential Result time:$fin s\n")
    return img
  }

  def parallel(filename:String):BufferedImage = {

    val t0 = System.nanoTime()
    val img = mf.do_all_par(filename)
    val t1 = System.nanoTime()
    val fin = (t1-t0) * 1.0 / 1E9
    println(s"Parallel Result time:$fin s\n")

    return img
  }

  def visualize(title:String, image:BufferedImage) ={
    val frame = new JFrame()

    frame.setTitle(title)
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    val panel = new JPanel()



    panel.add(new JLabel(new ImageIcon(image)))


    frame.add(panel)
    frame.setVisible(true)
    frame.pack()

  }

  def receive ={
    //should handle here between seq and parallel
    case s:String => visualize("parallel_result", parallel(s))
    case Bye => println("Bye!")

    case _ => println("Unknown param received")
  }

}

object EchoMain extends App{

  val system = ActorSystem("ImageProcessingSystem")

  // create and start the actor
  val echoActor = system.actorOf(Props[ImageActor], name = "ImageActor")
  val echoActor2 = system.actorOf(Props[ImageActor2], name = "ImageActor2")

  // prompt the user for input
  var input = ""
  while (input != "q") {
    print("Type name Image to process (q to quit): ")
    input = StdIn.readLine()
    echoActor ! input
    echoActor2 ! input
    // a brief pause so the actor can print its output
    // before this loop prints again
    Thread.sleep(200)
  }

  echoActor ! Bye
  echoActor2 !Bye
  // shut down the system
  system.terminate()

}
