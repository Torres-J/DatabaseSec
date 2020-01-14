## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-15698"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\WCN\Registrars).EnableRegistrars
$Regkey1 = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\WCN\Registrars).DisableUPnPRegistrar
$Regkey2 = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\WCN\Registrars).DisableInBand802DOT11Registrar
$Regkey3 = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\WCN\Registrars).DisableFlashConfigRegistrar
$Regkey4 = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\WCN\Registrars).DisableWPDRegistrar
if ($Regkey -eq '0' -and $Regkey1 -eq '0' -and $Regkey2 -eq '0' -and $Regkey3 -eq '0' -and $Regkey4 -eq '0')                                                                                                    
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