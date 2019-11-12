
package MedianFilter
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File

import scala.collection.parallel.ParSeq
import scala.collection.parallel
import scala.collection.parallel.mutable.ParArray

class MedianFilter {

    def read_image(filename:String ):BufferedImage  ={
      //need to find if the file exists in path
      return ImageIO.read(new File(filename))
    }

   def median_filter(kernel:Int, img: BufferedImage):BufferedImage ={
     val width = img.getWidth()
     val height = img.getHeight()
     var res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
     return res
   }

  def seq_median_filter(img:BufferedImage):BufferedImage = {

    val kernel = 3
    //val mid:Int = kernel / 2
    var neighs =  Array[Int](kernel * kernel)//should init to 0 all
    //neighs = Array.fill(kernel,kernel)(0)
    val height = img.getHeight()
    val width = img.getWidth()
    //check if position is near image borders

    var result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for( i <- 1 until width)
    {

      for(j <- 1 until height)
        {
          neighs(0) = img.getRGB(i - 1, j-1)
          neighs(1) = img.getRGB(i, j-1)
          neighs(2) = img.getRGB(i + 1, j-1)
          neighs(3) = img.getRGB(i-1, j)
          neighs(4) = img.getRGB(i, j)
          neighs(5) = img.getRGB(i + 1, j)
          neighs(6) = img.getRGB(i - 1, j+1)
          neighs(7) = img.getRGB(i, j+1)
          neighs(8) = img.getRGB(i + 1, j+1)

          neighs.sorted
          result.setRGB( i, j ,neighs(4))
        }

    }


    //sort after returning
    return result
  }

  def par_median_filter(img:BufferedImage):BufferedImage ={
    val kernel = 3
    var neighs = Array[Int](kernel * kernel)//should init to 0 all

    val height = img.getHeight()
    val width = img.getWidth()
    //check if position is near image borders

    var result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for( i <- (1 until width).par)
    {

      for(j <- (1 until height).par)
      {
        neighs(0) = img.getRGB(i - 1, j-1)
        neighs(1) = img.getRGB(i, j-1)
        neighs(2) = img.getRGB(i + 1, j-1)
        neighs(3) = img.getRGB(i-1, j)
        neighs(4) = img.getRGB(i, j)
        neighs(5) = img.getRGB(i + 1, j)
        neighs(6) = img.getRGB(i - 1, j+1)
        neighs(7) = img.getRGB(i, j+1)
        neighs(8) = img.getRGB(i + 1, j+1)

        neighs.sorted
        result.setRGB( i, j ,neighs(4))
      }

    }


    //sort after returning
    return result
  }

  def save_result_img(img:BufferedImage, name:String)={
    ImageIO.write(img, "jpg", new File(name))
  }

  def do_all_seq(filename:String): BufferedImage = {

      val img = read_image(filename)
      val res = seq_median_filter(img)
      save_result_img(res, "result.jpg")
      return res
  }

  def do_all_par(filename:String): BufferedImage = {

    val img = read_image(filename)
    val res = par_median_filter(img)
    save_result_img(res, "result.jpg")
    return res
  }


}
