package com.ino9dev.ai

import java.io._
import java.nio.ByteBuffer
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.layout.StackPane
import javafx.scene.canvas._
import javafx.scene.image.Image
import javafx.scene.transform.Affine
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import javafx.animation._
import com.sun.javafx.geom.transform.Affine2D

/**
 * 
 * DeepLearning Sample App
 * 
 * @source ISBN 978-4-86487-433-5
 * @author ino9dev<ino9dev@gmail.com>
 * @description 2016/02/03 newcreation
 * 
 */
object DeepLearningApp extends App {
  
  val row = 10
  val column = 10
  Data.readImage("C:\\Users\\oni\\git_forscala\\scalasample\\scalasample\\datas\\train-images\\train-images.idx3-ubyte", 100)
  Data.readLabel("C:\\Users\\oni\\git_forscala\\scalasample\\scalasample\\datas\\train-images\\train-labels.idx1-ubyte", 100)
  for(i <- 0 until row; j <- 0 until column){
    printf("%d ", Data.labelData(i * column + j))
    if(j == column-1)println
  }
  new Visualizer(28, 28, row, column).dispDataImage(Data.getData.flatten)
}

/**
 * Visulizer
 *  学習状態を可視化
 */
class Visualizer(val width:Int, val height: Int, val row: Int, val column: Int, val scale : Double = 1.0){
  
  var imgData : Array[Byte] = null
  var g : GraphicsContext = null
  
  new javafx.embed.swing.JFXPanel
  javafx.application.Platform.runLater(new Runnable {
    override def run() {
      while(imgData == null) Thread.sleep(1)
      val stage = new Stage
      val canvas = new Canvas(scale * ((width + 1)*column+1),scale*((height+1)*row+1))
      val pane = new StackPane
      pane.getChildren.add(canvas)
      stage.setScene(new Scene(pane))
      stage.show
      g = canvas.getGraphicsContext2D
      val affine = new Affine()
      affine.setMxx(scale)
      affine.setMxy(0)
      affine.setTx(0)
      affine.setMyx(0)
      affine.setMyy(scale)
      affine.setTy(0)
      g.setTransform(affine)
      new AnimationTimer {override def handle(now: Long) {draw}}.start
    }
  })
  def draw() {
    for(i <- 0 until row; j<-0 until column){
      g.drawImage(getImage(i*column + j), j*(width+1)+1, i*(height+1)+1)
    }
  }
  def getImage(idx: Int): Image = {
    val buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val len = width * height
    val off = len * idx
    buf.setRGB(0,0,width,height,imgData.slice(off,off+len).map(grayScale(_)),0,width)
    val out = new ByteArrayOutputStream
    try {
      ImageIO.write(buf, "bmp" , out)
      out.flush
      val img = new Image(new ByteArrayInputStream(out.toByteArray))
      out.close
      img
    } catch {
      case e: IOException => println(e)
      null
    }
  }
  
  /*
   * 1バイトデータを16bitへ変換
   */
  def grayScale(b: Byte) = {
    val v = b & 0xFF
    (v << 16) | (v << 8) | v
  }
  
  /*
   * 入出力の画像を表示する 
   */
  def dispDataImage(data: Array[Double]) {
   val rg = data.max
   imgData = data.map{ x => (x /rg * 255).toByte}
  }
  
  /*
   * 重み画像を表示する
   */
  def dispWeightImage(weight: Array[Array[Double]]) {
    imgData = weight.map { v =>  
      val mi = v.min
      val rg = v.max - mi
      v.map(x => ((x -mi)/rg*255).toByte)}.flatten
  }
}

/**
 * AutoEncoder
 * 　　オートエンコーダ
 *   教師なし学習を用いたニューラルネットワーク
 *   n: 入力層の次元数
 *   m: 中間層の次元数
 */
class AE(val n: Int, val m: Int){
  
  val y = new Array[Double](m) //中間層
  val z = new Array[Double](n) //出力層
  val w = Array.fill[Double](m, n)((math.random*2-1)*0.01) //ランダム
  val b1 = Array.fill[Double](m)(0.0) // バイアス1
  val b2 = Array.fill[Double](n)(0.0) // バイアス2
  val wDelta = Array.ofDim[Double](m, n) //重み修正値
  val b1Delta = new Array[Double](m) //中間層修正
  val b2Delta = new Array[Double](n) //出力層修正
  val b1Tmp = new Array[Double](m) //中間層作業変数
  val b2Tmp = new Array[Double](n) //出力層作業変数

  /*
   * sigmoid シグモイド関数
   *  ロジスティック関数
   */
  def sigmoid(x: Double) = {1 / (1+ math.pow(math.E, -x))}
  
  /*
   * encode エンコード関数
   *   入力層から中間層を算出する
   *   入力層の各層に重みをかけて、バイアスを加えたものを中間層とする
   */
  def encode(n:Int)(m:Int)(x: Array[Double], y: Array[Double]) {
//    (0 to m).map(i=>y(i)=sigmoid(
//        ))
  }

  /*
   * decode デコード関数
   */
  def decode(y: Array[Double], z: Array[Double]) {}
  
}

/**
 * Data
 *  手書きデータを読み込む人
 */
object Data {
  var imgData, labelData: Array[Byte] = null
  var width, height = 0
  var buf = new Array[Byte](4)
  
  def readInt(st: BufferedInputStream) = {
    st.read(buf)
    ByteBuffer.wrap(buf).getInt
  }
  
  def readFile(filename: String)(fun:(BufferedInputStream)=>Unit) {
    var st: BufferedInputStream = null
    try {
      st = new BufferedInputStream(new FileInputStream(filename))
      readInt(st) //magic numberを読み飛ばす
      fun(st) //funに処理させる
    } catch {
      case e : Exception => println(e)
    } finally {
      if(st!=null) st.close
    }
  }
  
  /**
   * Imageデータの読み込み
   */
  def readImage(fileName: String, m :Int = 0) = {
    readFile(fileName) { st =>
      val n = readInt(st)
      height = readInt(st)
      width = readInt(st)
      val size  = height * width * ( if (m > 0) m else n) //?
      imgData = new Array[Byte](size)
      val len = st.read(imgData, 0, size)
      printf("Image loaded: %d/%d\n", len/(height*width),n)
    }
    getData
  }
  
  /**
   * ラベルデータの読み込み
   */
  def readLabel(fileName: String, m: Int = 0) = {
    readFile(fileName) { st =>
      val n = readInt(st)
      val size = if(m>0) m else n
      labelData = new Array[Byte](size)
      val len = st.read(labelData, 0, size)
      printf("Label loaded: %d/%d\n", len, n)
    }
  }
  
  def getData() = {
    val len = width * height
    imgData.map(v => (v & 0xFF) /255.0).grouped(len).toArray
  }
  
  def getLabel() = {
    labelData.map {v=>
      val a = new Array[Double](10)
      a(v) = 1.0
      a
    }
  }
}