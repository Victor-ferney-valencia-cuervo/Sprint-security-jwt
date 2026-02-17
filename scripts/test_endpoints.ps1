$headers = @{ Authorization = 'Basic YWRtaW46YWRtaW4xMjM='; 'Content-Type' = 'application/json' }
$body = '{"title":"Prueba","isbn":"111","publicationYear":2023,"synopsis":"Sinopsis prueba"}'
try {
    $r = Invoke-WebRequest -Uri 'http://localhost:8080/api/v1/books' -Method Post -Headers $headers -Body $body -ErrorAction Stop
    Write-Output "STATUS: $($r.StatusCode)"
    Write-Output "CONTENT: $($r.Content)"
} catch {
    if ($_.Exception.Response -ne $null) {
        $sr = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $respBody = $sr.ReadToEnd()
        Write-Output "ERROR RESPONSE: $respBody"
    } else {
        Write-Output $_.Exception.Message
    }
}
