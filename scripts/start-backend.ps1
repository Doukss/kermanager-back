param(
    [switch]$SkipCompile
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot

$services = @(
    @{ Name = "discovery-service"; Port = 8761 },
    @{ Name = "api-gateway"; Port = 8080 },
    @{ Name = "auth-service"; Port = 8081 },
    @{ Name = "agency-service"; Port = 8082 },
    @{ Name = "property-service"; Port = 8083 },
    @{ Name = "payment-service"; Port = 8084 },
    @{ Name = "dispute-service"; Port = 8085 }
)

Push-Location $root
try {
    if (-not $SkipCompile) {
        Write-Host "Compiling backend..."
        .\mvnw.cmd -q -DskipTests compile
        if ($LASTEXITCODE -ne 0) {
            throw "Backend compilation failed"
        }
    }

    foreach ($service in $services) {
        $title = "Kermanager - $($service.Name) :$($service.Port)"
        $command = "cd /d `"$root`" && title $title && .\mvnw.cmd -pl $($service.Name) spring-boot:run"
        Write-Host "Starting $($service.Name) on port $($service.Port)..."
        Start-Process -FilePath "cmd.exe" -ArgumentList "/k", $command
        Start-Sleep -Seconds 3
    }

    Write-Host ""
    Write-Host "Backend startup launched."
    Write-Host "Gateway: http://localhost:8080"
    Write-Host "Discovery: http://localhost:8761"
    Write-Host "Swagger Auth: http://localhost:8081/swagger-ui.html"
}
finally {
    Pop-Location
}
