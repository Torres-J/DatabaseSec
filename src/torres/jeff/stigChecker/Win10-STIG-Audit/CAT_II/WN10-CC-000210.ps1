## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63685"
$GroupTitle = "WN10-CC-000210"
$RuleID = "SV-78175r6_rule"
$Severity = "CAT II"



if ($Hostname -eq $null){

$Hostname = $env:computername}
$RuleVersionSTIGID = "WN10-CC-000210"
$RuleTitle = "The Windows Defender SmartScreen for Explorer must be enabled."
$CCI = "CCI-000381"

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\System\").EnableSmartScreen
if ($regkey -eq '1'){
    $Configuration = 'Completed'}
    else{
    $Configuration = 'Ongoing'}



$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit