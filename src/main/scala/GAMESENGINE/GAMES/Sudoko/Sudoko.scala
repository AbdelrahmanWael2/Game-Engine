
package GAMES.Sudoko

import GAMESENGINE.Drawable

import java.awt.{BasicStroke, Color, Dimension, Font, Graphics, Graphics2D, Image, Stroke}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel
import scala.sys.process.BasicIO.input
import scala.util.Random
import scala.util.control.Breaks.break

object Sudoko {
  val size = 9
  val drawables: Array[Array[Drawable]] = Array.ofDim[Drawable](size, size)



  val images: Map[Int, String] = Map(
    1 -> "1.png",
    2 -> "2.png",
    3 -> "3.png",
    4 -> "4.png",
    5 -> "5.png",
    6 -> "6.png",
    7 -> "7.png",
    8 -> "8.png",
    9 -> "9.png"
  )

  case class symbol( var x: Int, var y: Int ,var img: Image) extends Drawable

  val board: Array[Array[Int]] = Array(
    Array(5, 3, 4, 6, 7, 8, 9, 1, 2),
    Array(6, 7, 2, 1, 9, 5, 3, 4, 8),
    Array(1, 9, 8, 3, 4, 2, 5, 6, 7),
    Array(8, 5, 9, 7, 6, 1, 4, 2, 3),
    Array(4, 2, 6, 8, 5, 3, 7, 9, 1),
    Array(7, 1, 3, 9, 2, 4, 8, 5, 6),
    Array(9, 6, 1, 5, 3, 7, 2, 8, 4),
    Array(2, 8, 7, 4, 1, 9, 6, 3, 5),
    Array(3, 4, 5, 2, 8, 6, 1, 7, 9)
  )


  //initialize starting board

  val rand = new Random()
  val threshold = 0.4 // Keep 40% of cells

  for (i <- board.indices; j <- board(i).indices) {
    if (rand.nextDouble() > threshold) {
      val value = board(j)(i)



      val image = ImageIO.read(new File(s"src/main/scala/GAMES/Sudoko/assets/${images(value)}"))
        .getScaledInstance(10, 10, Image.SCALE_SMOOTH)
      drawables(j)(i) = symbol(i * 2 + 1 , j * 2 + 1 , image)
    }
  }

  private def drawSymbol(d: Drawable, gfx: Graphics): Unit = {
    if (d != null)
      gfx.drawImage(d.img,  (d.x) *27,   (d.y) *27, null)}

  private def drawBoard(gfx: Graphics): Unit = {
    val font = new Font("default", Font.BOLD, 13)
    val alpha = Array("a", "b", "c", "d", "e", "f", "g", "h", "i")
    val num = Array("1", "2", "3", "4", "5", "6", "7", "8", "9")
    gfx.setFont(font)
    gfx.setColor(Color.BLACK)

    //draw grid
    for (i <- 0 to size) {
      val g2d = gfx.asInstanceOf[Graphics2D] // cast to Graphics2D
      g2d.setStroke(new BasicStroke(2))
      if (i % 3 == 0) g2d.setStroke(new BasicStroke(3))
      else g2d.setStroke(new BasicStroke(1))
      gfx.drawLine(i * 54, 0, i * 54, 480)
      gfx.drawLine(0, i * 54, 480, i * 54)
    }
    for (y <- 0 until 9) {
      val x = 515
      gfx.drawString(alpha(y), x , y * 55 + 25)
      if (x == 8) {
        gfx.drawString((y + 1).toString, x , y * 55 + 40)
      }
    }
    for (x <- 0 until 9) {
      val y = 515
      gfx.drawString(num(x), x *55 + 25, y )
      if (y == 8) {
        gfx.drawString((x + 1).toString, x* 55 + 40, y )
      }
    }

//    for (y <- 0 until 9; x <- 0 until 9) {
//
//    alpha.foreach { letter =>
//      for (y <- 0 until 10) {
//        gfx.drawString(letter, if (letter == alpha(0)) 0 else x* 55, y * 2 * 25)
//      }
//    }
      }

    def draw(input: String): JPanel = {
      val panel = new JPanel() {
        override def paint(gfx: Graphics): Unit = {
          super.paintComponent(gfx)

          drawBoard(gfx)
          drawables.foreach(_.foreach(drawSymbol(_, gfx)))
        }
      }
      panel setPreferredSize new Dimension(800,800)
      panel
    }

  def controller(input: String): Boolean = {
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i')
    if (!(columns.contains(input(1))) || !input(0).isDigit || !(input.length == 3) || !(input(2).isDigit))
      return false
    val x = input.substring(0, 1).toInt-1
    val y = columns.indexOf(input(1), 0)
    val z = input.substring(2, 3).toInt
    if (x > 9 || y > 9)
      return false

    //already exist
    if (!(drawables(y)(x) == null))
      return false

    if(!(z == board(y)(x))){
      return false
    }
    val image = ImageIO.read(new File(s"src/main/scala/GAMES/Sudoko/assets/${images(z)}"))
      .getScaledInstance(10, 10, Image.SCALE_SMOOTH)

    drawables(y)(x) = symbol(x * 2 + 1, y * 2 + 1, image)



    true
  }
}