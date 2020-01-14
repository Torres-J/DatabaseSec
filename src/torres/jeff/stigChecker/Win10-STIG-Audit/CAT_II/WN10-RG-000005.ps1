## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63593"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ChildItem -Path Cert:Localmachine\disallowed | Where Issuer -Like "*CCEB Interoperability*").Thumbprint
if ($regkey -contains '929BF3196896994C0A201DF4A5B71F603FEFBF2E' -and '929BF3196896994C0A201DF4A5B71F603FEFBF2E'){
    $regkeydate = (Get-ChildItem -Path Cert:Localmachine\disallowed | Where {$_.Issuer -Like "*DoD Interoperability*" -and $_.Subject -Like "*DoD*"}).NotAfter
     if($regkeydate.year -lt $date.year) {
        $Configuration = 'Ongoing'}
        else{
        $Configuration = 'Completed'}}
    else{
    $Configuration = 'Ongoing'}




$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit
