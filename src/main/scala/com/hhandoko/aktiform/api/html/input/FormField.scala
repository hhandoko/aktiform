package com.hhandoko.aktiform.api.html.input

sealed trait FormField[T] {
  def id: String
  def name: String
  def label: String
  def value: Option[T]
  def placeholder: Option[String]
  def required: Boolean
  def errors: Seq[FormFieldError]

  def parse(value: String): FormField[T]
}

/**
  * See: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/text
  *
  * TODO:
  *   - autocomplete - https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes/autocomplete
  *   - list
  *   - maxlength
  *   - minlength
  *   - pattern
  *   - readonly
  *   - size
  *   - spellcheck
  */
final case class InputTextField(
    id: String,
    name: String,
    label: String,
    value: Option[String] = None,
    placeholder: Option[String] = None,
    required: Boolean = false,
    errors: Seq[FormFieldError] = Seq.empty
) extends FormField[String] {
  // TODO: Add parsing and validation
  override def parse(value: String): FormField[String] =
    this.copy(value = Option(value.trim))
}

/**
  * See: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/textarea
  *
  * Skipped:
  *   - cols: Textarea width would be controlled by the stylesheet instead.
  *   - form: Textarea would always be a child of `<form>` for simplicity.
  *
  * TODO:
  *   - autocomplete - https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes/autocomplete
  *   - autofocus
  *   - disabled
  *   - maxlength
  *   - minlength
  *   - readonly
  *   - spellcheck
  *   - wrap
  */
final case class InputTextAreaField(
    id: String,
    name: String,
    label: String,
    value: Option[String] = None,
    placeholder: Option[String] = None,
    required: Boolean = false,
    errors: Seq[FormFieldError] = Seq.empty,
    rows: Option[Int] = None
) extends FormField[String] {
  // TODO: Add parsing and validation
  override def parse(value: String): FormField[String] =
    this.copy(value = Option(value.trim))
}

/**
  * See: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/number
  *
  * TODO:
  *   - autocomplete - https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes/autocomplete
  *   - list
  *   - max
  *   - min
  *   - readonly
  *   - step
  */
final case class InputNumberField(
    id: String,
    name: String,
    label: String,
    value: Option[Int] = None,
    placeholder: Option[String] = None,
    required: Boolean = false,
    errors: Seq[FormFieldError] = Seq.empty
) extends FormField[Int] {
  // TODO: Add parsing and validation
  override def parse(value: String): FormField[Int] =
    this.copy(value = Option(value.trim.toInt))
}
