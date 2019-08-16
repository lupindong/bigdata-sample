package net.lovexq.samplebidata.support

/**
  * ${DESCRIPTION}
  *
  * @author LuPindong
  * @time 2019-08-15 14:10
  */
class SecondarySortKey(val first: Int, val second: Int) extends Ordered[SecondarySortKey] with Serializable {

  override def compare(other: SecondarySortKey): Int = {
    if (this.first - other.first != 0) {
      this.first - other.first
    } else {
      this.second - other.second
    }
  }

  override def toString: String = {
    s"SecondarySortKey[${this.first}, ${this.second}]"
  }
}