## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63323"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (get-tpm).Tpmready
if ($regkey -eq 'True'){
    $tpm = (get-tpm).ManufacturerVersion
    if ($tpm -gt '1.2'){
        $Configuration = 'Completed'}
        else{
        $Configuration = 'Ongoing'}
}
else{
$Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit