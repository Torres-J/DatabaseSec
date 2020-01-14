## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-80153"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Adobe Acrobat Professional DC Classic Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_USERS\S-1-5-21*\SOFTWARE\Policies\Adobe\Adobe Acrobat\2015\FeatureLockDown\cDigSig\cAdobeDownload" -ErrorAction SilentlyContinue).bLoadSettingsFromURL
if ($Regkey -eq '0'){
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