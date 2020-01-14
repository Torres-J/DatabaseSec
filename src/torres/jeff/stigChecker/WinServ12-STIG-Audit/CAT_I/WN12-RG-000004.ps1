## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-1152"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-acl Registry::HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\SecurePipeServers\winreg).access | out-file $env:temp/result1152.txt | out-null
$CFG = $(gc C:\Users\ALEXC~1.BLA\AppData\Local\Temp\result1152.txt)

if ($CFG -contains "RegistryRights    : ReadKey" -and "AccessControlType : Allow" -and "IdentityReference : NT AUTHORITY\LOCAL SERVICE" -and "IsInherited       : False" -and "InheritanceFlags  : ContainerInherit" -and "PropagationFlags  : None" -and "RegistryRights    : FullControl" -and "AccessControlType : Allow" -and "IdentityReference : BUILTIN\Administrators" -and "IsInherited       : False" -and "InheritanceFlags  : ContainerInherit" -and "PropagationFlags  : None" -and "RegistryRights    : ReadKey" -and "AccessControlType : Allow" -and "IdentityReference : BUILTIN\Backup Operators" -and "IsInherited       : False" -and "InheritanceFlags  : None" -and "PropagationFlags  : None")
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