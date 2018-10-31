package components

import slinky.core.CustomAttribute

object Attributes {

  val ariaControls = new CustomAttribute[String]("aria-controls")
  val ariaDescribedBy = new CustomAttribute[String]("aria-describedby")
  val ariaExpanded = new CustomAttribute[Boolean]("aria-expanded")
  val ariaHidden = new CustomAttribute[Boolean]("aria-hidden")
}
