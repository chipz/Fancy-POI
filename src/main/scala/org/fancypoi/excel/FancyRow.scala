package org.fancypoi.excel

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.util.CellReference.convertColStringToIndex
import org.apache.poi.ss.util.CellReference.convertNumToColString
import org.fancypoi.Implicits._
import org.fancypoi.excel.FancyExcelUtils._

class FancyRow(protected[fancypoi] val _row: Row) {

  override def toString = "#" + _row.getSheet.getSheetName + "!*" + addr

  def addr = (_row.getRowNum + 1).toString

  def apply(address: String): Cell = cell(address)

  def apply(index: Int): Cell = cellAt(index)

  def cell(address: String): Cell = cellAt(convertColStringToIndex(address))

  def cellAt(index: Int) = _row.getCell(index, Row.CREATE_NULL_AS_BLANK)

  def cell_?(address: String) = cellAt_?(convertColStringToIndex(address))

  def cellAt_?(index: Int) = !!(_row.getCell(index, Row.RETURN_NULL_AND_BLANK))

  def cells: List[Cell] = (0 to lastColIndex).map(cellAt).toList

  def firstColAddr = convertNumToColString(firstColIndex)

  def firstColIndex = _row.getFirstCellNum.toInt

  def lastColAddr = convertNumToColString(lastColIndex)

  def lastColIndex = _row.getLastCellNum.toInt

  def index = _row.getRowNum

  def cellsFrom(startColAddr: String)(block: CellSeq => Unit) {
    cellsFromAt(convertColStringToIndex(startColAddr))(block)
  }

  def cellsFromAt(startColIndex: Int)(block: CellSeq => Unit) {
    block(new CellSeq(_row, startColIndex))
  }

  def find(query: String) = {
    cells.filter(_.value.isDefined).find { cell =>
      import FancyCellType._
      val cellValueStr = cell.cellType match {
        case CellTypeNumeric => cell.value.get.toString
        case CellTypeString  => cell.value.get.toString
        case CellTypeFormula => ""
        case CellTypeBoolean => cell.value.get.toString
        case _ => ""
      }
      cellValueStr == query
    }.map(x => x.addr)
  }

  private class CellSeq(row: Row, colIndex: Int) {
    var current = colIndex

    def apply(block: Cell => Unit) {
      block(row.cellAt(current))
      current += 1
    }
  }

}
