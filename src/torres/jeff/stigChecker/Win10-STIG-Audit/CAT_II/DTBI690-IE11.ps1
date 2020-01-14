## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-46807"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Microsoft Internet Explorer 11 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = Get-ItemProperty Registry::"HKEY_USERS\S-1-5-21*\Software\Policies\Microsoft\Internet Explorer\Main"
$Val = $RegKey."Use FormSuggest"
if ($Val -eq 'no'){
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit