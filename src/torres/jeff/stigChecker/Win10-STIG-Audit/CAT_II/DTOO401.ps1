﻿## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-40858"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Microsoft Office System 2013 STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKLM\software\policies\Microsoft\office\15.0\common\officeupdate").EnableAutomaticUpdates
$Regkey1 = (Get-ItemProperty Registry::"HKLM\software\policies\Microsoft\Windows\WindowsUpdate").WUServer
$Regkey2 = (Get-ItemProperty Registry::"HKLM\software\policies\Microsoft\Windows\WindowsUpdate").WUStatusServer
if ($Regkey -eq '1' -and $Regkey1 -match 'ar.ds.army.mil' -and $Regkey2 -match 'ar.ds.army.mil'){
    $Configuration = 'Completed'}
    else
    {
    $Configuration = 'Ongoing'}
    
    



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit