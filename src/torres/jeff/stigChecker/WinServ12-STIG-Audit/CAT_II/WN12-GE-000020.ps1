## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-15823"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$drives = get-psdrive
$Configuration = ""
$Regkey = foreach ($drive in $drives){
get-childitem -include *.p12,*.pfx -Recurse -File -ErrorAction SilentlyContinue}

if ($Regkey -eq '0' -or [string]::IsNullOrEmpty($Regkey) -eq "True") 

{
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit