# User Stories
## OCR Extraktion
Als Student möchte ich, dass Text aus Bildern die ich zur verfügung stelle, extrahiert und gespeichert wird, damit ich aus diesem Text später Karteikarten
erstellen kann.

### Anforderungen
- Es können "Projekte" angelegt werden
- Ein Projekt kann eine Reihe von Dokumenten und Bildern beinhalten.
Die Dokumente und Bilder können hochgeladen werden.
- Für die Bilder kann eine Textextraktion veranlasst werden. Die Textextratktion
kann für ein einzelnes Bild oder für eine Reihe von Bildern veranlasst werden.
Der extrahierte Text ist mit dem Bild bzw. den Bildern verknüpft.
- Der extrahierte Text aus den Bildern enthält eine Reihe von Metainformationen:
    - Ein Extraktionszeitstempel
    - Extrahiert: Ein Kennzeichen, woraus der Text extrahiert wurde. Möglich sind initial "Image" und "Document". Später könnten noch weitere hinzukommen.
    - Editiert: ein Kennzeichen, ob der Text nach der Extraktion editiert wurde.
- Der extrahierte Text kann später auch noch bearbeitet werden. Auf der Seite,
auf der der Text geändert werden kann, wird auch das Bild angezeigt (links das Bild, rechts der Text).
- Für die Dokumente kann eine Textextraktion veranlasst werden.
- Der extrahierte Text wird mit dem Dokument verknüpft und später noch 
geändert werden. Auf der Seite zum editieren des Textes kann das verknüpfte
Dokument heruntergeladen werden.
- Der extrahierte Text aus Dokumenten enthält eine Reihe von Metainformationen:
    - Ein Extraktionszeitstempel
    - Extrahiert: Ein Kennzeichen, woraus der Text extrahiert wurde. Möglich sind initial "Image" und "Document". Später könnten noch weitere hinzukommen.
    - Editiert: ein Kennzeichen, ob der Text nach der Extraktion editiert wurde.

## Technische Anforderungen
- Die verwendete API wird über einen Adapter angesprochen, der wiederum ein Port-Interface implementiert. Dadurch kann leicht eine andere API
verwendet werden, ohne den Anwendungskern (die domain) zu ändern.
- Für die Extraktion von Text aus Bildern (OCR) wird die API von Mistral 
verwendet.
- Der API Endpunkt ist: https://api.mistral.ai/v1/ocr
- Die Authorisierung erfolgt via HTTP Authorization Scheme: bearer
- Als API-Key kann "abv" verwendet werden.
- Der Endpunkt wird via REST "POST" angesprochen
- Das model, das im Request angegeben werden muss, heißt "mistral-ocr-latest"
- Die maximale Größe der gesendeten Dokumente/Bilder darf 50 MB nicht überschreiten. 
- Beispielaufruf des Endpunkts via CURL mit einem Base64 encoded image:
```text
curl https://api.mistral.ai/v1/ocr \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${MISTRAL_API_KEY}" \
  -d '{
    "model": "mistral-ocr-latest",
    "document": {
        "type": "image_url",
        "image_url": "data:image/jpeg;base64,<base64_image>"
    },
    "include_image_base64": true
  }' -o ocr_output.json
```
- Die Payload für den Request ist im folgenden dargestellt:
```json
{
  "model": "string",
  "id": "string",
  "document": {
    "type": "file",
    "file_id": "8a0cfb4f-ddc9-436d-91bb-75133c583767"
  },
  "pages": [
    0
  ],
  "include_image_base64": true,
  "image_limit": 0,
  "image_min_size": 0,
  "bbox_annotation_format": {
    "type": "text",
    "json_schema": {
      "name": "string",
      "description": "string",
      "schema": {},
      "strict": false
    }
  },
  "document_annotation_format": {
    "type": "text",
    "json_schema": {
      "name": "string",
      "description": "string",
      "schema": {},
      "strict": false
    }
  }
}
```
- Die Felder der Paylod werden im folgenden beschrieben
```text
model (required): Model (string) or Model (null) (Model) 
id: string (id)
document: FileChunk (object) or DocumentURLChunk (object) or ImageURLChunk (object) (Document). Document to run OCR on
    FileChunk
        type: string (Type)
            Default: "file"
            Value: "file"
        file_id (required): string <uuid> (File id)
    DucumentURLChunk
        initial nicht berücksichtigt
    ImageURLChunk
        initial nicht berücksichtigt
pages: Array of Pages (integers) or Pages (null) (Pages). Specific pages user wants to process in various formats: single number, range, or list of both. Starts from 0
    Array [integer] or null
include_image_base64: Include Image Base64 (boolean) or Include Image Base64 (null) (Include Image Base64). Include image URLs in response
image_limit: Image Limit (integer) or Image Limit (null) (Image Limit). Max images to extract
image_min_size: Image Min Size (integer) or Image Min Size (null) (Image Min Size). Minimum height and width of image to extract
bbox_annotation_format: ResponseFormat (object) or null. Structured output class for extracting useful information from each extracted bounding box / image from document. Only json_schema is valid for this field.
    ResponseFormat
        type: string (ResponseFormat)
            Default: "text"
            Enum: "text" "json_object" "json_schema". An object specifying the format that the model must output. Setting to { "type": "json_object" } enables JSON mode, which guarantees the message the model generates is in JSON. When using JSON mode you MUST also instruct the model to produce JSON yourself with a system or a user message.
        json_schema: JsonSchema (object) or null
            name (required): string (Name)
            description: Description (string) or Description (null) (Description)
            schema (required): object (Schema)
                property name*: any
            strict: boolean (Strict). Default false
document_annotation_format: ResponseFormat (object) or null. Structured output class for extracting useful information from the entire document. Only json_schema is valid for this field
    ResponseFormat
        type: string (ResponseFormat)
            Default: "text"
            Enum: "text" "json_object" "json_schema". An object specifying the format that the model must output. Setting to { "type": "json_object" } enables JSON mode, which guarantees the message the model generates is in JSON. When using JSON mode you MUST also instruct the model to produce JSON yourself with a system or a user message.
        json_schema: JsonSchema (object) or null
            name (required): string (Name)
            description: Description (string) or Description (null) (Description)
            schema (required): object (Schema)
                property name*: any
            strict: boolean (Strict). Default false

```
Ein POST auf den Endpunkt kann mit einem HTTP-Statuscode 200 oder 422 (Validation Error) beantwortet werden. 
Response 200 (Successful Response):
```json
{
  "pages": [
    {
      "index": 0,
      "markdown": "string",
      "images": [
        {
          "id": "string",
          "top_left_x": 0,
          "top_left_y": 0,
          "bottom_right_x": 0,
          "bottom_right_y": 0,
          "image_base64": "string",
          "image_annotation": "string"
        }
      ],
      "dimensions": {
        "dpi": 0,
        "height": 0,
        "width": 0
      }
    }
  ],
  "model": "string",
  "document_annotation": "string",
  "usage_info": {
    "pages_processed": 0,
    "doc_size_bytes": 0
  }
}
```
Response 422 (Validation Error):
```json
{
  "detail": [
    {
      "loc": [
        "string"
      ],
      "msg": "string",
      "type": "string"
    }
  ]
}
```

## Hinweise
- Im root-Verzeichnis dieses Projekts gibt es die Bilddateien ocr-test1.jpg und ocr-test2.jpg mit denen die Implementierung getestet werden kann.
- Das frontend ist im Verzeichnis "frontend". Das Backend im Verzecihnis "src".
