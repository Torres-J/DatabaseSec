## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63403"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = "Completed"
#$Regkey = 
#if ($regkey.ReleaseId -gt '1703'){
#    $Configuration = 'PASS V-63403: Operating System meets minimum build number'}
#    else{
#    $Configuration = 'FAIL V-63403: Operating System DOES NOT meet minimum build number'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit