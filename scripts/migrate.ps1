param(
    [string]$DbUrl = "jdbc:postgresql://localhost:5432/kermanager",
    [string]$DbUser = "immo",
    [string]$DbPassword = "immo_secret"
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot

$migrations = @(
    @{ Name = "auth";     Location = "auth-service/src/main/resources/db/migration";     Table = "flyway_schema_history_auth" },
    @{ Name = "agency";   Location = "agency-service/src/main/resources/db/migration";   Table = "flyway_schema_history_agency" },
    @{ Name = "property"; Location = "property-service/src/main/resources/db/migration"; Table = "flyway_schema_history_property" },
    @{ Name = "payment";  Location = "payment-service/src/main/resources/db/migration";  Table = "flyway_schema_history_payment" },
    @{ Name = "dispute";  Location = "dispute-service/src/main/resources/db/migration";  Table = "flyway_schema_history_dispute" }
)

Push-Location $root
try {
    foreach ($migration in $migrations) {
        Write-Host "Migrating $($migration.Name)-service..."
        .\mvnw.cmd -q -N `
            "-Dflyway.url=$DbUrl" `
            "-Dflyway.user=$DbUser" `
            "-Dflyway.password=$DbPassword" `
            "-Dflyway.locations=filesystem:$($migration.Location)" `
            "-Dflyway.table=$($migration.Table)" `
            flyway:migrate

        if ($LASTEXITCODE -ne 0) {
            Write-Host "Creating baseline for $($migration.Name)-service..."
            .\mvnw.cmd -q -N `
                "-Dflyway.url=$DbUrl" `
                "-Dflyway.user=$DbUser" `
                "-Dflyway.password=$DbPassword" `
                "-Dflyway.locations=filesystem:$($migration.Location)" `
                "-Dflyway.table=$($migration.Table)" `
                "-Dflyway.baselineVersion=0" `
                flyway:baseline

            if ($LASTEXITCODE -ne 0) {
                throw "Flyway baseline failed for $($migration.Name)-service"
            }

            .\mvnw.cmd -q -N `
                "-Dflyway.url=$DbUrl" `
                "-Dflyway.user=$DbUser" `
                "-Dflyway.password=$DbPassword" `
                "-Dflyway.locations=filesystem:$($migration.Location)" `
                "-Dflyway.table=$($migration.Table)" `
                flyway:migrate

            if ($LASTEXITCODE -ne 0) {
                throw "Flyway migrate failed for $($migration.Name)-service"
            }
        }
    }
}
finally {
    Pop-Location
}
