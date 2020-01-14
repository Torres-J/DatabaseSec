﻿## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-57721"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (get-acl C:\Windows\System32\WINEVT\LOGS\Eventvwr.exe -ErrorAction SilentlyContinue).access | Where-Object{
$_.identityreference -eq "BUILTIN\Users"  -and ($_.RegistryRights -match "FullControl")}


if ($Regkey -eq $null)                                                                                                     
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

