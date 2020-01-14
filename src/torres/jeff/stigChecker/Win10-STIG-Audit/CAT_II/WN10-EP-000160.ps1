
## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-77223"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ProcessMitigation -Name  java.exe).DEP.enable
$Regkey3 = (Get-ProcessMitigation -Name  java.exe).Payload.EnableExportAddressFilter
$Regkey4 = (Get-ProcessMitigation -Name  java.exe).Payload.EnableExportAddressFilterPlus
$Regkey5 = (Get-ProcessMitigation -Name  java.exe).Payload.EnableImportAddressFilter
$Regkey6 = (Get-ProcessMitigation -Name  java.exe).Payload.EnableRopStackPivot
$Regkey7 = (Get-ProcessMitigation -Name  java.exe).Payload.EnableRopCallerCheck
$Regkey8 = (Get-ProcessMitigation -Name  java.exe).Payload.EnableRopSimExec

$Regkey9 = (Get-ProcessMitigation -Name  javaw.exe).DEP.enable
$Regkey13 = (Get-ProcessMitigation -Name  javaw.exe).Payload.EnableExportAddressFilter
$Regkey14 = (Get-ProcessMitigation -Name  javaw.exe).Payload.EnableExportAddressFilterPlus
$Regkey15 = (Get-ProcessMitigation -Name  javaw.exe).Payload.EnableImportAddressFilter
$Regkey16 = (Get-ProcessMitigation -Name  javaw.exe).Payload.EnableRopStackPivot
$Regkey17 = (Get-ProcessMitigation -Name  javaw.exe).Payload.EnableRopCallerCheck
$Regkey18 = (Get-ProcessMitigation -Name  javaw.exe).Payload.EnableRopSimExec

$Regkey0 = (Get-ProcessMitigation -Name  javaws.exe).DEP.enable
$Regkey03 = (Get-ProcessMitigation -Name  javaws.exe).Payload.EnableExportAddressFilter
$Regkey04 = (Get-ProcessMitigation -Name  javaws.exe).Payload.EnableExportAddressFilterPlus
$Regkey05 = (Get-ProcessMitigation -Name  javaws.exe).Payload.EnableImportAddressFilter
$Regkey06 = (Get-ProcessMitigation -Name  javaws.exe).Payload.EnableRopStackPivot
$Regkey07 = (Get-ProcessMitigation -Name  javaws.exe).Payload.EnableRopCallerCheck
$Regkey08 = (Get-ProcessMitigation -Name  javaws.exe).Payload.EnableRopSimExec

$check = New-Object -TypeName System.Object
$check | Add-Member -MemberType NoteProperty -Name result -Value $Regkey
$check | Add-Member -MemberType NoteProperty -Name result3 -Value $Regkey3
$check | Add-Member -MemberType NoteProperty -Name result4 -Value $Regkey4
$check | Add-Member -MemberType NoteProperty -Name result5 -Value $Regkey5
$check | Add-Member -MemberType NoteProperty -Name result6 -Value $Regkey6
$check | Add-Member -MemberType NoteProperty -Name result7 -Value $Regkey7
$check | Add-Member -MemberType NoteProperty -Name result8 -Value $Regkey8

$check | Add-Member -MemberType NoteProperty -Name result9 -Value $Regkey9
$check | Add-Member -MemberType NoteProperty -Name result13 -Value $Regkey13
$check | Add-Member -MemberType NoteProperty -Name result14 -Value $Regkey14
$check | Add-Member -MemberType NoteProperty -Name result15 -Value $Regkey15
$check | Add-Member -MemberType NoteProperty -Name result16 -Value $Regkey16
$check | Add-Member -MemberType NoteProperty -Name result17 -Value $Regkey17
$check | Add-Member -MemberType NoteProperty -Name result18 -Value $Regkey18

$check | Add-Member -MemberType NoteProperty -Name result0 -Value $Regkey0
$check | Add-Member -MemberType NoteProperty -Name result03 -Value $Regkey03
$check | Add-Member -MemberType NoteProperty -Name result04 -Value $Regkey04
$check | Add-Member -MemberType NoteProperty -Name result05 -Value $Regkey05
$check | Add-Member -MemberType NoteProperty -Name result06 -Value $Regkey06
$check | Add-Member -MemberType NoteProperty -Name result07 -Value $Regkey07
$check | Add-Member -MemberType NoteProperty -Name result08 -Value $Regkey08

$result = ($check.result,$check.result2,$check.result3,$check.result4,$check.result5,$check.result6,$check.result7,$check.result8,$check.result9,$check.result13,$check.result14,$check.result15,$check.result16,$check.result17,$check.result18,$check.result0,$check.result03,$check.result04,$check.result05,$check.result06,$check.result07,$check.result08)

if ($result -notmatch 'ON'){
    $Configuration = 'Ongoing'}
    else{
    $Configuration = 'Completed'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit