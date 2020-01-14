## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-17605"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Microsoft Office System 2013 STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_USERS\S-1-5-21*\Software\Policies\Microsoft\Office\15.0\common\documentinformationpanel").Beaconing
if ($Regkey -eq '1'){
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit