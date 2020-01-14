## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-4443"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-ItemProperty Registry::HKEY_LOCAL_MACHINE\system\CurrentControlSet\Control\SecurePipeServers\winreg\AllowedPaths).machine | out-file $env:temp/result4443.txt | out-null
$CFG = $(gc C:\Users\ALEXC~1.BLA\AppData\Local\Temp\result4443.txt)

if ($CFG -contains "Software\Microsoft\Windows NT\CurrentVersion\Print" -and "Software\Microsoft\Windows NT\CurrentVersion\Windows" -and "System\CurrentControlSet\Control\Print\Printers" -and "System\CurrentControlSet\Services\Eventlog,Software\Microsoft\OLAP Server" -and "System\CurrentControlSet\Control\ContentIndex" -and "System\CurrentControlSet\Control\Terminal Server" -and "System\CurrentControlSet\Control\Terminal Server\UserConfig" -and" System\CurrentControlSet\Control\Terminal Server\DefaultUserConfiguration" -and "Software\Microsoft\Windows NT\CurrentVersion\Perflib" -and "System\CurrentControlSet\Services\SysmonLog")
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