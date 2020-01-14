## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-40237"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows Server 2012/2012 R2 Domain Controller STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}
$Date = Get-Date
$Configuration = ""
$certs = Get-ChildItem -Path Certificate::Localmachine\root | Where Subject -Like "*CCEB Interoperability*" | select NotAfter
foreach($cert in $certs){
    $cert| select -ExpandProperty NotAfter
    

if ($cert.notafter -lt $Date) 

{
    $Configuration = 'Ongoing'}
    else{
    $Configuration = 'Completed'}

    if ($Configuration -eq 'Ongoing'){break}
        
    }
$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit