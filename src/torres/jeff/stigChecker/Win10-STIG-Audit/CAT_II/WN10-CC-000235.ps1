## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63701"
$GroupTitle = "WN10-CC-000235"
$RuleID = "SV-78191r6_rule"
$Severity = "CAT II"
$RuleVersionSTIGID = "WN10-CC-000235"
$RuleTitle = "Windows 10 must be configured to prevent Microsoft Edge browser data from being cleared on exit."
$CCI = "CCI-000366"

$Configuration = ""
$Regkey = (Get-ItemProperty Registry::"HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\MicrosoftEdge\PhishingFilter\").PreventOverride
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