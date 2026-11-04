# Genera ficheros .mp3 de ejemplo con cabecera ID3v1 valida, organizados en
# subcarpetas (para poder probar el escaneo RECURSIVO del ejercicio 1 de la
# practica de la unidad 2). El "audio" es relleno (no son ficheros reproducibles),
# pero la cabecera ID3v1 de los ultimos 128 bytes es real y valida.

function New-Mp3ConID3v1 {
    param(
        [string]$Ruta,
        [string]$Titulo,
        [string]$Artista,
        [string]$Album,
        [string]$Anio,
        [string]$Comentario,
        [byte]$Genero
    )

    function PadTo([string]$s, [int]$len) {
        [byte[]]$bytes = New-Object byte[] $len
        $enc = [System.Text.Encoding]::GetEncoding("ISO-8859-1").GetBytes($s)
        [Array]::Copy($enc, $bytes, [Math]::Min($enc.Length, $len))
        , $bytes
    }

    $tag = New-Object System.Collections.Generic.List[byte]
    $tag.AddRange([System.Text.Encoding]::ASCII.GetBytes("TAG"))
    $tag.AddRange((PadTo $Titulo 30))
    $tag.AddRange((PadTo $Artista 30))
    $tag.AddRange((PadTo $Album 30))
    $tag.AddRange((PadTo $Anio 4))
    $tag.AddRange((PadTo $Comentario 30))
    $tag.Add($Genero)

    # "Audio" de relleno: unos cientos de bytes, para que el fichero no sea
    # solo la cabecera y tenga sentido "saltar hasta los ultimos 128 bytes".
    $audioFalso = New-Object byte[] 512
    (New-Object Random).NextBytes($audioFalso)

    $contenido = New-Object System.Collections.Generic.List[byte]
    $contenido.AddRange($audioFalso)
    $contenido.AddRange($tag.ToArray())

    [System.IO.File]::WriteAllBytes($Ruta, $contenido.ToArray())
    Write-Output "Creado: $Ruta ($($contenido.Count) bytes)"
}

$base = Join-Path $PSScriptRoot "mp3-ejemplo"
New-Item -ItemType Directory -Path (Join-Path $base "rock") -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $base "pop") -Force | Out-Null

# Genero ID3v1: 17 = Rock, 13 = Pop
New-Mp3ConID3v1 -Ruta (Join-Path $base "rock\bohemian_rhapsody.mp3") `
    -Titulo "Bohemian Rhapsody" -Artista "Queen" -Album "A Night at the Opera" `
    -Anio "1975" -Comentario "SonoTeca ejemplo" -Genero 17

New-Mp3ConID3v1 -Ruta (Join-Path $base "rock\radio_ga_ga.mp3") `
    -Titulo "Radio Ga Ga" -Artista "Queen" -Album "The Works" `
    -Anio "1984" -Comentario "SonoTeca ejemplo" -Genero 17

New-Mp3ConID3v1 -Ruta (Join-Path $base "pop\dancing_queen.mp3") `
    -Titulo "Dancing Queen" -Artista "ABBA" -Album "Arrival" `
    -Anio "1976" -Comentario "SonoTeca ejemplo" -Genero 13

New-Mp3ConID3v1 -Ruta (Join-Path $base "pop\waterloo.mp3") `
    -Titulo "Waterloo" -Artista "ABBA" -Album "Waterloo" `
    -Anio "1974" -Comentario "SonoTeca ejemplo" -Genero 13

# Un fichero que NO tiene cabecera ID3v1 valida, para poder probar ese caso
# (el ejercicio 2 debe devolver null en vez de lanzar una excepcion).
$sinTag = New-Object byte[] 300
(New-Object Random).NextBytes($sinTag)
[System.IO.File]::WriteAllBytes((Join-Path $base "pop\sin_etiquetas.mp3"), $sinTag)
Write-Output "Creado (sin ID3v1, para probar el caso limite): $(Join-Path $base 'pop\sin_etiquetas.mp3')"
