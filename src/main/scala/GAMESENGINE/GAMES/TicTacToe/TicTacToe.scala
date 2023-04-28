package GAMES.TicTacToe

import GAMESENGINE.Drawable

import java.awt.{Color, Dimension, Font, Graphics, Image}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel

object TicTacToe {


  val drawables: Array[Array[Drawable]] = Array.ofDim[Drawable](3, 3)





  var turn = 0
  //get pics
  private val XSymbol :Image =
    ImageIO.read(new File("src/main/scala/GAMES/TicTacToe/Assets/x.png"))
      .getScaledInstance(140, 140, Image.SCALE_SMOOTH)

  private val OSymbol :Image =
    ImageIO.read(new File("src/main/scala/GAMES/TicTacToe/Assets/o.jpeg"))
      .getScaledInstance(140, 140, Image.SCALE_SMOOTH)



  private case class Symbol(a: Int, b: Int, sym: Image) extends Drawable {
    override var x: Int = a
    override var y: Int = b
    override var img: Image = sym
  }



  private def drawBoard(gfx: Graphics): Unit = {
    val font = new Font("default", Font.BOLD, 13)
    val alpha = Array("a", "b", "c")
    var white = true

    gfx.setFont(font)

    for (y <- 0 until 3; x <- 0 until 3) {
      val color = if (white) new Color(220, 220, 220) else new Color(120, 120, 120)
      gfx.setColor(color)
      gfx.fillRect(10 + x * 160, 15 + y * 160, 160, 160)

      white = !white
      gfx.setColor(Color.black)
      gfx.drawString((x + 1).toString, 90 + x * 160, 10)
      gfx.drawString((x + 1).toString, 90 + x * 160, 510)
    }

    alpha.foreach { letter =>
      for (y <- 0 until 3) {
        gfx.drawString(letter, if (letter == alpha(0)) 0 else 490, 95 + y * 160)
      }
    }
  }


  //enforce game rules
  //return false if violated
  def controller(input:String):Boolean ={
    val columns = Array('a', 'b', 'c')
    if(!(columns.contains(input(1))) || !input(0).isDigit || !(input.length==2))
      return false
    //indices
    val x = input.substring(0, 1).toInt - 1
    val y = columns.indexOf(input(1), 0)

    if(x > 2 || y > 2)
      return false

    //already exist
    if (!(drawables(x)(y) == null))
      return false

    

    if(turn % 2 == 0){
      drawables(x)(y) = Symbol(x, y, XSymbol)
    }else{
      drawables(x)(y) = Symbol(x, y, OSymbol)
    }

    //toggle turn
    turn = turn ^ 1
    true
  }



  private def drawSymbol(d: Drawable, gfx: Graphics): Unit =
    if (d != null)
      gfx.drawImage(d.img, 10 + d.x * 165, 15 + d.y * 165, null)

  def draw(input: String): JPanel = {
    val panel = new JPanel() {
      override def paint(gfx: Graphics): Unit = {
        super.paintComponent(gfx)
        drawBoard(gfx)
        drawables.foreach(_.foreach(drawSymbol(_, gfx)))
      }
    }
    panel setPreferredSize new Dimension(800, 500)
    panel
  }
}

