## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63337"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$BLinfo = Get-Bitlockervolume

if($blinfo.ProtectionStatus -eq 'On' -and $blinfo.EncryptionPercentage -eq '100'){
    $Configuration = "Completed"
}else{
$Configuration = "Ongoing"}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit