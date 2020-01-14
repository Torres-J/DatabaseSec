## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-80127"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Adobe Acrobat Professional DC Classic Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_USERS\S-1-5-21*\Software\Adobe\Adobe Acrobat\2015\AVGeneral" -ErrorAction SilentlyContinue).bFIPSMode
if ($Regkey -eq '1'){
    $Configuration = 'Completed'}
    elseif ($Regkey -eq $null -or '0')
    {
    $Configuration = 'Ongoing'}
    
    



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit