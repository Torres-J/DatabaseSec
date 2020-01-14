## Windows 10 STIG Compliance Audit
## Version 1, Release 1
## Created by Alex Blackburn


$GroupID = "V-78129"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$GPOid = (get-gpo -guid 66F8FF4F-94FD-412C-AAFA-516990D0CCAE)

if ($GPOid.GpoStatus -eq 'AllSettingsEnabled '){$Configuration = 'Completed'}else{$Configuration = 'Ongoing'}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit