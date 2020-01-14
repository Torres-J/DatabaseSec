## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-1081"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (get-volume).filesystem | out-file $env:temp/result1081.txt | out-null
$CFG = $(gc C:\Users\ALEXC~1.BLA\AppData\Local\Temp\result1081.txt)

if ($CFG -contains "FAT32")                                                                                                     
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