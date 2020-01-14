## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-32282"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-acl Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Active Setup\Installed Components").access | Where-Object{
$_.identityreference -eq "BUILTIN\Users"  -and ($_.RegistryRights -match "ReadKey")}


if ($Regkey -eq $null)                                                                                                     
{
    $Configuration = 'Ongoing'}
    else{
    $Configuration = 'Completed'}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit