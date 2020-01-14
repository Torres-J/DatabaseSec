
## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-77217"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ProcessMitigation -Name GROOVE.EXE).DEP.enable
$Regkey1 = (Get-ProcessMitigation -Name GROOVE.EXE).ASLR.BottomUp
$Regkey2 = (Get-ProcessMitigation -Name GROOVE.EXE).ASLR.ForceRelocateImages
$Regkey3 = (Get-ProcessMitigation -Name GROOVE.EXE).Payload.EnableExportAddressFilter
$Regkey4 = (Get-ProcessMitigation -Name GROOVE.EXE).Payload.EnableExportAddressFilterPlus
$Regkey5 = (Get-ProcessMitigation -Name GROOVE.EXE).Payload.EnableImportAddressFilter
$Regkey6 = (Get-ProcessMitigation -Name GROOVE.EXE).Payload.EnableRopStackPivot
$Regkey7 = (Get-ProcessMitigation -Name GROOVE.EXE).Payload.EnableRopCallerCheck
$Regkey8 = (Get-ProcessMitigation -Name GROOVE.EXE).Payload.EnableRopSimExec

$check = New-Object -TypeName System.Object
$check | Add-Member -MemberType NoteProperty -Name result -Value $Regkey
$check | Add-Member -MemberType NoteProperty -Name result1 -Value $Regkey1
$check | Add-Member -MemberType NoteProperty -Name result2 -Value $Regkey2
$check | Add-Member -MemberType NoteProperty -Name result3 -Value $Regkey3
$check | Add-Member -MemberType NoteProperty -Name result4 -Value $Regkey4
$check | Add-Member -MemberType NoteProperty -Name result5 -Value $Regkey5
$check | Add-Member -MemberType NoteProperty -Name result6 -Value $Regkey6
$check | Add-Member -MemberType NoteProperty -Name result7 -Value $Regkey7
$check | Add-Member -MemberType NoteProperty -Name result8 -Value $Regkey8
$check | Add-Member -MemberType NoteProperty -Name result9 -Value $Regkey9
$result = ($check.result,$check.result1,$check.result2,$check.result3,$check.result4,$check.result5,$check.result6,$check.result7,$check.result8)

if ($result -match 'NOTSET' -or 'False'){
    $Configuration = 'Ongoing'}
    else{
    $Configuration = 'Completed'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit