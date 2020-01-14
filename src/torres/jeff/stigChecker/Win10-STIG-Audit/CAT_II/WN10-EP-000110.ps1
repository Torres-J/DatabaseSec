
## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-77205"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ProcessMitigation -Name firefox.exe).DEP.enable
$Regkey1 = (Get-ProcessMitigation -Name firefox.exe).ASLR.BottomUP
$Regkey2 = (Get-ProcessMitigation -Name firefox.exe).ASLR.ForceRelocateImages
if ($Regkey -eq 'ON' -and $Regkey1 -eq 'ON' -and $Regkey2 -eq 'ON'){
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit