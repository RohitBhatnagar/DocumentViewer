# DocumentViewer
It is a Document Viewer using BOX REST API and can be run using Spring Boot

<b>Prerequisites:</b>

1. Box.com account
2. Spring Boot Environment

<b>Steps to run:</b>

1. Edit the "application.properties" file and add the Developer Token from your Box Application.
2. Get the Developer Token by running your custom application with JWT using OAuth2.0
3. Add the location where you want to download on your local machine as well.
4. Run the app as a "Spring Boot App"
5. Open postman/browser and call the API "http://localhost:{port}/docviewer/{required API call}"

Eg: To get the Root Folder info, hit this API
"http://localhost:9568/docviewer/folder/0"

<b>Available API's:</b>
1. Getting File info <b>"docviewer/file/{fileId}"</b>
2. Getting Folder info <b>"docviewer/folder/{folderId}"</b>
3. Getting All items in the Folder <b>"docviewer/folder/{folderId}/items"</b>
4. Downloading a file to local machine <b>"docviewer/file/{fileId}/download"</b>
5. Downloading all the contents of the folder to a local machine <b>"docviewer/folder/{folderId}/download"</b>
