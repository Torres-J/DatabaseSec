## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-40776"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Microsoft Lync 2013 STIG"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKLM\Software\Policies\Microsoft\office\15.0\lync" -ErrorAction SilentlyContinue).savepassword
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