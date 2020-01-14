## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63393"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$PFX = Get-ChildItem -Path .\ -Filter *.pfx -Recurse -File -Name| ForEach-Object {
    [System.IO.Path]::GetFileNameWithoutExtension($_)
    }
$P12 = Get-ChildItem -Path .\ -Filter *.p12 -Recurse -File -Name| ForEach-Object {
    [System.IO.Path]::GetFileNameWithoutExtension($_)
    }
$Result = $PFX -and $P12

if ($Result -match 'False'){
    $Configuration = "Completed"
}else{
$Configuration = "Ongoing"}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit