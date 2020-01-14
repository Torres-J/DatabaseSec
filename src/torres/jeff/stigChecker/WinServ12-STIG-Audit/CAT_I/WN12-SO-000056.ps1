## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-3339"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\system\CurrentControlSet\Control\SecurePipeServers\winreg\AllowedExactPaths).machine | out-file $env:temp/result3339.txt | out-null
$CFG = $(gc C:\Users\ALEXC~1.BLA\AppData\Local\Temp\result3339.txt)

if ($CFG -contains "System\CurrentControlSet\Control\ProductOptions" -and "System\CurrentControlSet\Control\Server Applications" -and "Software\Microsoft\Windows NT\CurrentVersion")
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