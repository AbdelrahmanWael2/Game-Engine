package GAMES.Connect4

import GAMESENGINE.{Drawable}

import java.awt.{Color, Dimension, Font, Graphics, Image}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel

object connect {

  val drawables: Array[Array[Drawable]] = Array.ofDim[Drawable](6, 7)
  var turn = 0
  // init colors
  val backGroundColor = new Color(51, 153, 255)
  val initCircleColor = new Color(255, 255, 255)
  val writeColor = new Color(0, 0, 0)
  // init dimentions
  val boarder: Int = 5
  val space: Int = 2
  val cellDimention = 75
  val boardWidth = 2 * boarder + 6 * space + 7 * cellDimention
  val boardHeight = 2 * boarder + 5 * space + 6 * cellDimention
  val startX: Int = (800 - boardWidth) / 2
  val startY: Int = 15
  // init images
  private val redCircle: Image =
    ImageIO
      .read(new File("src/main/scala/GAMES/Connect4/assests/Red-Circle.png"))
      .getScaledInstance(75, 75, Image.SCALE_SMOOTH)
  private val yellowCircle: Image =
    ImageIO
      .read(new File("src/main/scala/GAMES/Connect4/assests/yellow-circle.png"))
      .getScaledInstance(75, 75, Image.SCALE_SMOOTH)


  private case class Symbol(a: Int, b: Int, sym: Image) extends Drawable {
    override var x: Int = a
    override var y: Int = b
    override var img: Image = sym
  }

  // draw empty grid
  private def drawInitBoard(gfx: Graphics) {
    val font = new Font("default", Font.BOLD, 13)
    // draw blue background
    gfx.setFont(font)
    gfx.setColor(backGroundColor)
    gfx.fillRect(startX, startY, boardWidth, boardHeight)
    // draw empty cells
    gfx.setColor(initCircleColor)
    for (i <- 0 to 6) {
      for (j <- 0 to 5)
        gfx.fillOval(
          startX + boarder + i * (cellDimention + space),
          startY + boarder + j * (cellDimention + space),
          cellDimention,
          cellDimention
        )
    }

    // draw rows names
    gfx.setColor(writeColor)
    val alpha = Array("a", "b", "c", "d", "e", "f", "g")
    for (i <- 0 to 6) {
      gfx.drawString(
        alpha(i),
        startX + boarder + 38 + (cellDimention + space) * i,
        startY - 5
      )
    }

    // draw cols numbers
    for (i <- 0 to 5) {
      gfx.drawString(
        (6 - i).toString(),
        startX - 10,
        startY + boarder + 38 + (cellDimention + space) * i
      )
    }
  }

  // draw colored cell
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

  def controller(input:String): Boolean = {
    val columns = Array('a', 'b', 'c', 'd', 'e', 'f', 'g')
    if (
      !(columns.contains(input(1))) || !
        input(0)
        .isDigit || !(input.length == 2)
    )
      return false

    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)

    if (x > 5 || y > 6)
      return false
    if (!(drawables(x)(y) == null))
      return false
    if ((x != 0) && (drawables(x - 1)(y) == null))
      return false

    val i = y
    val j = 6 - x
    if (turn % 2 == 0) {
      drawables(x)(y) = Symbol(
        startX + boarder + i * (cellDimention + space) + 1,
        startY + boarder + (j - 1) * (cellDimention + space) + 1,
        redCircle
      )
    } else {
      drawables(x)(y) = Symbol(
        startX + boarder + i * (cellDimention + space),
        startY + boarder + (j - 1) * (cellDimention + space),
        yellowCircle
      )
    }
    turn = 1 - turn
     true
  }

}
