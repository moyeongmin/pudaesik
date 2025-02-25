package com.example.bbudaesik.data.model

data class NotionResponse(
    val results: List<NotionPage>
)

data class NotionPage(
    val id: String,
    val properties: NotionProperties
)

data class NotionProperties(
    val mealDate: RichTextProperty?,
    val mealKindGcd: RichTextProperty?,
    val codeNm: RichTextProperty?,
    val mealNm: RichTextProperty?
)

data class RichTextProperty(
    val rich_text: List<RichTextContent>?
)

data class RichTextContent(
    val text: TextContent
)

data class TextContent(
    val content: String
)