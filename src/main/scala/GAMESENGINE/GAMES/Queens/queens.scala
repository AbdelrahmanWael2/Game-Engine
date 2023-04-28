package GAMES.Queens

import GAMESENGINE.{Drawable}

import java.awt.{Color, Dimension, Font, Graphics, Image}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel
import GAMES.Connect4.connect

object queens {


  val drawables: Array[Array[Drawable]] = Array.ofDim[Drawable](8, 8)

  // init colors
  val writeColor = new Color(0, 0, 0)
  val black = new Color(128, 128, 128)
  val white = new Color(255, 255, 255)
//   val cols = new Array[Boolean](8)
//   val rows = new Array[Boolean](8)
  val played = new Array[String](8)
  var num = 0;
  // init image
  private val queen: Image =
    ImageIO
      .read(new File("src/main/scala/GAMES/Queens/assests/queen.png"))
      .getScaledInstance(40, 40, Image.SCALE_SMOOTH)

  // init dimentions
  val startY: Int = 15
  val cellDimention = 50
  val startX: Int = (800 - 8 * cellDimention) / 2


  private case class Symbol(a: Int, b: Int, sym: Image) extends Drawable {
    override var x: Int = a
    override var y: Int = b
    override var img: Image = sym
  }

  // graw empty board
  private def drawInitBoard(gfx: Graphics) {
    val font = new Font("default", Font.BOLD, 13)
    gfx.setFont(font)

    // draw rows names
    gfx.setColor(writeColor)
    val alpha = Array("a", "b", "c", "d", "e", "f", "g", "h")
    for (i <- 0 to 7) {
      gfx.drawString(
        alpha(i),
        startX + 25 + (cellDimention) * i,
        startY - 5
      )
    }

    // draw cols numbers
    for (i <- 0 to 7) {
      gfx.drawString(
        (8 - i).toString(),
        startX - 10,
        startY + 25 + (cellDimention) * i
      )
    }

    for (i <- 0 to 7) {
      for (j <- 0 to 7) {
        if (i % 2 == j % 2)
          gfx.setColor(white)
        else
          gfx.setColor(black)
        gfx.fillRect(
          startX + i * cellDimention,
          startY + j * cellDimention,
          cellDimention,
          cellDimention
        )

      }
    }
  }

  // draw queen
  private def drawSymbol(d: Drawable, gfx: Graphics) =
    if (d != null)
      gfx.drawImage(d.img, d.x, d.y, null)

  // draw all
  def draw(input:String): JPanel = {
    val panel = new JPanel() {
      override def paint(gfx: Graphics) {
        super.paintComponent(gfx)
        drawInitBoard(gfx)
        drawables.foreach(_.foreach(drawSymbol(_, gfx)))
      }
    }
    panel setPreferredSize new Dimension(800, 500)
    panel
  }

  // controler
  def controller(input:String): Boolean = {
    if (num == 8)
      return false
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    if (
      !(columns.contains(input(1))) || !
        input(0)
        .isDigit || !(input.length == 2)
    )
      return false
    for (i <- 0 to num - 1) {
      if (
        input(0) == played(i)(0) || input(1) == played(i)(1) || math
          .abs(input(0).toInt - played(i)(0))
          .toInt == math.abs(
          columns.indexOf(input(1), 0) - columns.indexOf(played(i)(1), 0)
        )
      )
        return false
    }
    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)
    if (x > 7 || y > 7)
      return false

    drawables(x)(y) = Symbol(
      startX + y * cellDimention + 5,
      startY + (7 - x) * cellDimention + 5,
      queen
    )
    played(num) = input
    num = num + 1
     true
  }
}
