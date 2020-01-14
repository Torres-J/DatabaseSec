## Windows 10 STIG Compliance Audit
## Created by Trevor Bryant

$GroupID = "V-63363"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$BUGroup = Get-LocalGroupMember -Name "Backup Operators"
If ($BUGroup -eq $null){
    $Configuration = "Completed"
}else{
$Configuration = "Ongoing"}


$Audit = New-Object -TypeName System.Object
$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit