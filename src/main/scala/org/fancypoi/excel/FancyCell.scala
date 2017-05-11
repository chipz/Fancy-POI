package org.fancypoi.excel

import org.apache.poi.ss.usermodel.{ RichTextString, Hyperlink, Font, CellStyle, Cell }
import org.apache.poi.ss.util.CellReference.convertNumToColString
import java.util.{ Calendar, Date }
import org.fancypoi.Implicits._
import org.fancypoi.excel.FancyExcelUtils._

class FancyCell(protected[fancypoi] val _cell: Cell) {
  lazy val workbook = _cell.getSheet.getWorkbook

  override def toString = "#" + _cell.getSheet.getSheetName + "!" + addr

  def styleFont = workbook.getFontAt(style.getFontIndex)

  def addr: String = convertNumToColString(_cell.getColumnIndex) + (_cell.getRowIndex + 1)

  def value: Option[Any] = {
    import FancyCellType._
    cellType match {
      case CellTypeNumeric => Some(numericValue)
      case CellTypeString  => Some(stringValue)
      case CellTypeFormula => Some(formula)
      case CellTypeBlank   => None
      case CellTypeBoolean => Some(booleanValue)
      case CellTypeError   => Some(errorValue)
    }
  }

  //force convert value to string
  def valueToString: String = {
    import FancyCellType._
    cellType match {
      case CellTypeNumeric =>
        val str = numericValue.toString
        if (str.endsWith(".0")) {
          str.replace(".0", "")
        } else {
          str
        }
      case CellTypeString  => stringValue
      case CellTypeFormula => getCellResultFormulaAsString
      case CellTypeBlank   => ""
      case CellTypeBoolean => booleanValue.toString
      case CellTypeError     => ""
    }
  }

  def stringValue: String = _cell.getStringCellValue

  def forceStringValue: String = {
    val x = _cell.getCellType
    _cell.setCellType(Cell.CELL_TYPE_STRING)
    val y = _cell.getStringCellValue
    _cell.setCellType(x)
    y
  }

  def numericValue: Double = _cell.getNumericCellValue

  def richTextValue: RichTextString = _cell.getRichStringCellValue

  def dateValue: Date = _cell.getDateCellValue

  def booleanValue: Boolean = _cell.getBooleanCellValue

  def errorValue: Byte = _cell.getErrorCellValue

  def isEmpty: Boolean = cellType == FancyCellType.CellTypeBlank

  def value_=(value: String): Unit =
    _cell.setCellValue(value)

  def value_=(value: Double): Unit =
    _cell.setCellValue(value)

  def value_=(value: RichTextString): Unit =
    _cell.setCellValue(value)

  def value_=(value: Calendar): Unit =
    _cell.setCellValue(value)

  def value_=(value: Date): Unit =
    _cell.setCellValue(value)

  def value_=(value: Boolean): Unit =
    _cell.setCellValue(value)

  def formula: String = {
    _cell.getCellFormula
  }

  def formula_=(formula: String): Unit =
    _cell.setCellFormula(formula);

  def hyperlink_=(linkType: Int, address: String) = {
    val link = workbook.getCreationHelper.createHyperlink(linkType)
    link.setAddress(address)
    _cell.setHyperlink(link)
    this
  }

  def hyperlink: Hyperlink = _cell.getHyperlink

  def row = _cell.getRow

  def rowIndex = _cell.getRowIndex

  def colIndex = _cell.getColumnIndex

  def cellType: FancyCellType.CellType = FancyCellType.fromPoiCellType(_cell.getCellType)

  def style = _cell.getCellStyle

  def font = workbook.getFontAt(_cell.getCellStyle.getFontIndex)

  /**
   * フォントを更新します。
   * 変更した設定値以外は、既存の値を引き継ぎます。
   */
  def updateFont(block: Font => Unit) = {
    val updatedFont = workbook.getFontBasedWith(workbook.getFontAt(_cell.getCellStyle.getFontIndex))(block)
    updateStyle(_.setFont(updatedFont))
    this;
  }

  /**
   * フォントを新規に設定します。
   * 設定していない値には、デフォルトの値が設定されます。
   */
  def replaceFont(block: Font => Unit) = {
    val newFont = workbook.getFontWith(block)
    updateStyle(_.setFont(newFont))
    this;
  }

  /**
   * セルスタイルを置き換えます。
   */
  def replaceStyle(styleObj: CellStyle) = {
    _cell.setCellStyle(styleObj);
    this
  }

  /**
   * セルスタイルを更新します。
   * 変更した設定値以外は、既存の値を引き継ぎます。
   */
  def updateStyle(block: CellStyle => Unit) = {
    val updatedStyle = workbook.getStyleBasedWith(_cell.getCellStyle)(block)
    _cell.setCellStyle(updatedStyle)
    this;
  }

  /**
   * セルスタイルを新規に設定します。
   * 設定していない値には、デフォルトの値が設定されます。
   */
  def replaceStyle(block: CellStyle => Unit) = {
    val style = workbook.getStyle(block)
    _cell.setCellStyle(style)
    this
  }

  def nextCell = {
    _cell.getSheet.cellAt(_cell.getColumnIndex + 1, _cell.getRowIndex)
  }

  def nexCell(x: Int) = {
    _cell.getSheet.cellAt(_cell.getColumnIndex + x, _cell.getRowIndex)
  }

  def nextCellDown = {
    _cell.getSheet.cellAt(_cell.getColumnIndex, _cell.getRowIndex + 1)
  }

  def nextCellDown(x: Int) = {
    _cell.getSheet.cellAt(_cell.getColumnIndex, _cell.getRowIndex + x)
  }

  def getCellResultFormulaAsString = {
    _cell.getCachedFormulaResultType match {
      case Cell.CELL_TYPE_NUMERIC =>
        val str = _cell.numericValue.toString
        if (str.endsWith(".0")) {
          str.replace(".0", "")
        } else {
          str
        }
      case Cell.CELL_TYPE_STRING => _cell.getRichStringCellValue.toString
      case Cell.CELL_TYPE_ERROR => "0"
    }
  }

}
