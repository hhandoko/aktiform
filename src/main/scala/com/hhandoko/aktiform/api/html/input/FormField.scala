package com.hhandoko.aktiform.api.html.input

sealed trait FormField {
  def id: String
  def name: String
  def label: String
  def value: Option[String]
  def placeholder: Option[String]
  def required: Boolean
  def errors: Seq[FormFieldError]
}

/** See: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/text */
final case class InputTextField(
    id: String,
    name: String,
    label: String,
    value: Option[String] = None,
    placeholder: Option[String] = None,
    required: Boolean = false,
    errors: Seq[FormFieldError] = Seq.empty
) extends FormField

/** See: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/textarea */
final case class InputTextAreaField(
    id: String,
    name: String,
    label: String,
    value: Option[String] = None,
    placeholder: Option[String] = None,
    required: Boolean = false,
    errors: Seq[FormFieldError] = Seq.empty,
    rows: Option[Int] = None
) extends FormField
