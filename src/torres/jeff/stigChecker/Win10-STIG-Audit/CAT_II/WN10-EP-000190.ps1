﻿
## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-77233"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ProcessMitigation -Name MSPUB.EXE).DEP.enable
$Regkey1 = (Get-ProcessMitigation -Name MSPUB.EXE).ASLR.BottomUP
$Regkey2 = (Get-ProcessMitigation -Name MSPUB.EXE).ASLR.ForceRelocateImages
$Regkey3 = (Get-ProcessMitigation -Name  MSPUB.EXE).Payload.EnableExportAddressFilter
$Regkey4 = (Get-ProcessMitigation -Name  MSPUB.EXE).Payload.EnableExportAddressFilterPlus
$Regkey5 = (Get-ProcessMitigation -Name  MSPUB.EXE).Payload.EnableImportAddressFilter
$Regkey6 = (Get-ProcessMitigation -Name  MSPUB.EXE).Payload.EnableRopStackPivot
$Regkey7 = (Get-ProcessMitigation -Name  MSPUB.EXE).Payload.EnableRopCallerCheck
$Regkey8 = (Get-ProcessMitigation -Name  MSPUB.EXE).Payload.EnableRopSimExec
if ($Regkey -eq 'ON' -and $Regkey1 -eq 'ON' -and $Regkey2 -eq 'ON' -and $Regkey3 -eq 'ON' -and $Regkey4 -eq 'ON' -and $Regkey5 -eq 'ON' -and $Regkey6 -eq 'ON' -and $Regkey7 -eq 'ON' -and $Regkey8 -eq 'ON'){
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit