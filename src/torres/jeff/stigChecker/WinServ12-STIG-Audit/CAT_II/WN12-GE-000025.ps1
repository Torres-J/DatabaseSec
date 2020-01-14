## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-36736"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\AGMProgram\Build\Update\Installed\{B0BC5217-217A-46C1-8DF8-EBC74CF2A658}").applicationname

if ($Regkey -eq 'Axway Desktop Validator')                                                                                                     
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